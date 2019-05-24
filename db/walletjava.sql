drop database walletjava;

create database walletjava;

use walletjava;

create table auth(
	id int not null auto_increment,
    username varchar(50) not null unique,
    hashPassword varchar(50) not null,
    salt varchar(50) not null,
    primary key (id)
);

create table users(
    userid int not null unique,
    fullName varchar(100) character set utf8 collate utf8_unicode_ci not null,
    totalCash int not null,
    primary key (userid)
);

create table sessions (
	id int not null auto_increment,
    userid int not null,
	accessKey varchar(50) not null unique,
    primary key (id)
);

create table debt(
	id bigint not null auto_increment,
    userid int not null,
    onDate date not null,
    content text character set utf8 collate utf8_unicode_ci not null,
    amount int not null,
    positive tinyint(1) not null,
    paid tinyint(1) not null,
    primary key (id)
);

create table expenditure(
	id bigint not null auto_increment,
    userid int not null,
    onDate date not null,
    content text character set utf8 collate utf8_unicode_ci not null,
    amount int not null,
    positive tinyint(1) not null,
    primary key (id)
);

alter table users
add constraint fk_users_auth
foreign key (userid) references auth(id)
on delete restrict
on update cascade;

alter table sessions
add constraint fk_sessions_auth
foreign key (userid) references auth(id)
on delete restrict
on update cascade;

alter table debt
add constraint fk_debt_auth
foreign key (userid) references auth(id)
on delete restrict
on update cascade;

alter table expenditure
add constraint fk_expenditure_auth
foreign key (userid) references auth(id)
on delete restrict
on update cascade;