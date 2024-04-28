package com.ecust.test;

import cn.hutool.json.JSONUtil;
import com.ecust.book.mapper.BooksMapper;
import com.ecust.base.entity.Books;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class EsTest {
    private RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://192.168.116.50:9200")
    ));
    @Autowired
    private BooksMapper booksMapper;

    @Test
    public void test() throws IOException {
        List<Books> books = booksMapper.queryAllBooks();
        for(Books book:books){

            IndexRequest indexRequest = new IndexRequest("books").id(book.getId()+"");
            String s = JSONUtil.toJsonStr(book);
            indexRequest.source(s, XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }
}
