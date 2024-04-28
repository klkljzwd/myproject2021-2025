class Conf:
    def __init__(self) -> None:
        self.csv = "./dataset/us_std.csv"
        self.window_len = 10
        self.target = "OT"
        self.radio = [0.8,0.1,0.1]
        self.batch_size = 64
        self.model = "./saved_model/model_parm.pkl"
        self.is_reversed = True
        self.encoding = "utf8"
        self.isBid = False