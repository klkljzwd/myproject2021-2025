package com.ecust.book.service.impl;

import cn.hutool.json.JSONUtil;
import com.ecust.auth.advicer.NormalException;
import com.ecust.auth.advicer.UnauthorizationException;
import com.ecust.book.mapper.BooksMapper;
import com.ecust.auth.mapper.UserBookMapper;
import com.ecust.base.PageResult;
import com.ecust.base.ResponseResult;
import com.ecust.base.ThreadLocalAuth;
import com.ecust.base.annotation.AuthPermission;
import com.ecust.base.constant.Constant;
import com.ecust.base.dto.SearchBooksDTO;
import com.ecust.base.entity.Authority;
import com.ecust.base.entity.Books;
import com.ecust.book.service.BooksService;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BooksServiceImpl implements BooksService {
    private RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.116.50:9200")
    ));

    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserBookMapper userBookMapper;

    @Override
    @AuthPermission(level = 4)
    public PageResult searchBook(SearchBooksDTO searchBooksDTO) {
        Integer pageNum = searchBooksDTO.getCurrentPage().intValue();
        Integer pageSize = searchBooksDTO.getPageSize().intValue();
        SearchRequest searchRequest = new SearchRequest("books");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //构造查询条件
        if(searchBooksDTO.getInputText().equals("")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name",searchBooksDTO.getInputText()));
        }
        if (searchBooksDTO.getLabel()!=null&&!searchBooksDTO.getLabel().equals("")){
            boolQueryBuilder.filter(QueryBuilders.termQuery("type",searchBooksDTO.getLabel()));
        }
        //分页
        searchRequest.source().from((pageNum-1)*pageSize).size(pageSize);
        //将查询条件放入
        searchRequest.source().query(boolQueryBuilder);
        //结果高亮
        searchRequest.source().highlighter(new HighlightBuilder().field("name").requireFieldMatch(false)
                .preTags("<span style='color: red;'>")
                .postTags("</span>"));

        try {
            //获取结果
            List<Books> booksList = new ArrayList<>();
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //获取总页数
            TotalHits totalHits = search.getHits().getTotalHits();
            Long total = totalHits.value;
            SearchHit[] hits = search.getHits().getHits();
            for(SearchHit hit:hits){
                String sourceAsString = hit.getSourceAsString();
                Books books = JSONUtil.toBean(sourceAsString, Books.class);
                //获取高亮部分
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if(highlightFields!=null && highlightFields.size()!=0){
                    HighlightField name = highlightFields.get("name");
                    if(name!=null){
                        Text[] nameFragments = name.getFragments();
                        String nameHigh = nameFragments[0].string();
                        books.setName(nameHigh);
                    }

                }
                booksList.add(books);
            }
            ResponseResult responseResult = new ResponseResult();
            responseResult.setData(booksList);
            responseResult.setCode(200);
            PageResult pageResult = new PageResult();
            pageResult.setTotal(total);
            pageResult.setResponseResult(responseResult);
            return pageResult;

        } catch (IOException e) {
           throw new NormalException("查询错误");
        }
    }

    @Override
    @AuthPermission(level = 3)
    public ResponseResult lendBook(Long id) {
        //获取该图书
        Books bookById = booksMapper.getBookById(id);
        //如果是vip图书则进行认证
        if(bookById.getIsVip()==1){
            Long uid = ThreadLocalAuth.get();
            String s = stringRedisTemplate.opsForValue().get(Constant.userPrix + uid);
            List<Authority> authorities = JSONUtil.toList(s, Authority.class);
            int flag = 0;
            for(Authority authority:authorities){
                if(authority.getLevel()<=2){
                    flag++;
                }
            }
            if(flag==0){
                throw new UnauthorizationException("权限不足");
            }
        }
        //借书
        Long userId = ThreadLocalAuth.get();
        userBookMapper.addUserBook(userId,id);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        return responseResult;
    }

    @Override
    @AuthPermission(level = 1)
    @Transactional
    public ResponseResult addBook(Books book) {
        booksMapper.addBook(book);
        ResponseResult responseResult = new ResponseResult();
        //同步到es
        IndexRequest indexRequest = new IndexRequest("books").id(book.getId()+"");
        String s = JSONUtil.toJsonStr(book);
        indexRequest.source(s, XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseResult.setCode(200);
        responseResult.setMsg("添加成功");
        return responseResult;
    }

    @Override
    @AuthPermission(level = 1)
    @Transactional
    public ResponseResult updateBook(Books books) {
        //数据库先删
        booksMapper.deleteBook(books.getId());
        //删除原先es中的文档
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("books").id(books.getId()+"");
        try {
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //添加文档
        booksMapper.addBook(books);
        IndexRequest indexRequest = new IndexRequest("books").id(books.getId()+"");
        String s = JSONUtil.toJsonStr(books);
        indexRequest.source(s,XContentType.JSON);
        try {
            restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        responseResult.setMsg("添加成功");
        return responseResult;
    }

    @Override
    @AuthPermission(level = 1)
    @Transactional
    public ResponseResult deleteBook(Long id) {
       booksMapper.deleteBook(id);
       DeleteRequest deleteRequest = new DeleteRequest();
       deleteRequest.index("books").id(id+"");
        try {
            restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(200);
        responseResult.setMsg("删除成功");
        return responseResult;
    }
}
