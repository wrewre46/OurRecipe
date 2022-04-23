create table ourrecipe.board
(
    UserID VARCHAR(20) NOT NULL,
    BoardID int ,
    BoardTitle VARCHAR(200) NOT NULL,
    BoardContent VARCHAR(2048) NOT NULL,
    BoardAvailable int default 1,
    WriteDate date,
    WriteTime time,
    RecommendCount int default 0,
    primary key(BoardID)
);