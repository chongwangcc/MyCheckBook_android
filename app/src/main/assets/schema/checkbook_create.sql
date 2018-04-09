

create table AccountInfo (
account_id           INTEGER                        not null,
account_name         varchar(64),
primary key (account_id)
);

create table CheckbookInfo (
id                   INTEGER                        not null,
name                 varchar(64),
description          varchar(1024),
islocal              bool,
coverImage           blob,
primary key (id)
);

create table CheckDetails (
id                   INTEGER                        not null,
checkbook_id         INTEGER                        not null,
account_id           INTEGER,
date_str             Text,
year                 varchar(4),
month                varchar(2),
day                  varchar(2),
money                float,
description          varchar(128),
incomeStatement      varchar(64),
Categoryclassification varchar(16),
isCreditcard         bool,
updateTime           timestamp,
last_update_user_id  int,
primary key (id),
foreign key (checkbook_id)
      references CheckbookInfo (id),
foreign key (account_id)
      references AccountInfo (account_id)
);

create table CheckbookAccountMap (
checkbook_id         INTEGER,
account_id           INTEGER,
foreign key (checkbook_id)
      references CheckbookInfo (id),
foreign key (account_id)
      references AccountInfo (account_id)
);

create table UserCheckbookMap (
id                   INTEGER                        not null,
user_name            VARBINARY(0)                   not null,
account_id           INTEGER,
permission           int,
description          varchar(64),
primary key (id),
foreign key (account_id)
      references CheckbookInfo (id)
);

