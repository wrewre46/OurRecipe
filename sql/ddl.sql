create table ourrecipe.board
(
    MemberID VARCHAR(20) NOT NULL,
    MemberNickname varchar(20) ,
    BoardID int ,
    BoardTitle VARCHAR(200) NOT NULL,
    BoardContent VARCHAR(2048) NOT NULL,
    BoardAvailable int default 1,
    WriteDate date,
    WriteTime time,
    RecommendCount int default 0,
    primary key(BoardID)
);

create table ourrecipe.member
(
    ID int,
    MemberID Varchar(2048) not null,
    Password Varchar(2048) not null,
    Email varchar(30),
    Nickname varchar(30),
    Role varchar(20) not null,
    Provider varchar(20),
    ProviderID varchar(2048),
    MemberCreateDate date,
    MemberCreateTime time,
    primary key(ID)

);