create table ourrecipe.board
(
    UserID VARCHAR(20) NOT NULL,
    BoardID int ,
    BoardTitle VARCHAR(200) NOT NULL,
    BoardContent VARCHAR(2048) NOT NULL,
    BoardAvailable bit default 1,
    WriteTime datetime,
    RecommendCount int default 0,
    primary key(BoardID)
);