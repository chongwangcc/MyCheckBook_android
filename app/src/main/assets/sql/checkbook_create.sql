create table if not exists AccountInfo (
account_id           int                            not null,
account_name         varchar(64),
primary key (account_id)
);

create table if not exists CheckbookInfo (
id                   int                            not null,
name                 varchar(64),
description          varchar(1024),
islocal              bool,
coverImage           blob,
primary key (id)
);

create table if not exists CheckDetails (
id                   int                            not null,
checkbook_id         int                            not null,
account_id           int,
"date"               date,
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

create table if not exists CheckbookAcountMap (
checkbook_id         int,
account_id           int,
foreign key (checkbook_id)
      references CheckbookInfo (id),
foreign key (account_id)
      references AccountInfo (account_id)
);

create table if not exists UserInfo (
id                   int                            not null,
name                 varchar(16),
password             varchar(64),
permission           int,
description          Text,
primary key (id)
);

create table if not exists UserCheckbookMap (
user_id              int                            not null,
account_id           int,
permission           int,
description          varchar(64),
primary key (user_id),
foreign key (user_id)
      references UserInfo (id),
foreign key (account_id)
      references CheckbookInfo (id)
);

