create table AccountInfo (
account_id           varchar(128)                   not null,
account_name         varchar(64),
parent_id            int,
"level"              int,
key                  varchar(1024),
assets_nums          float,
liabilities_nums     float,
primary key (account_id)
);

create table CheckbookInfo (
id                   varchar(128)                   not null,
name                 varchar(64),
description          varchar(1024),
islocal              bool,
coverImage           blob,
primary key (id)
);

create table CheckDetails (
id                   varchar(128)                   not null,
checkbook_id         varchar(128)                   not null,
account_id           varchar(128),
last_update_user_id  varchar(128),
date_str             Text,
year                 varchar(4),
month                varchar(2),
day                  varchar(2),
money                float,
description          varchar(128),
balanceType          varchar(64),
Category             varchar(16),
isCreditcard         bool,
updateTime           long,
createTime           long,
primary key (id),
foreign key (checkbook_id)
      references CheckbookInfo (id),
foreign key (account_id)
      references AccountInfo (account_id)
);

create table CheckbookAccountMap (
checkbook_id         varchar(128),
account_id           varchar(128),
foreign key (checkbook_id)
      references CheckbookInfo (id),
foreign key (account_id)
      references AccountInfo (account_id)
);

create table UserCheckbookMap (
id                   varchar(128)                   not null,
user_name            VARBINARY(0)                   not null,
checkbook_id         INTEGER,
permission           int,
description          varchar(64),
primary key (id),
foreign key (checkbook_id)
      references CheckbookInfo (id)
);

