create table file_records
(
id 						INT(11) NOT NULL AUTO_INCREMENT,
path					varchar(2000),
backup_path				varchar(2000),
last_modified			varchar(50),
length					varchar(50),
summary					varchar(100), -- 64位
description				varchar(100),
created_at        		TIMESTAMP,
updated_at        		TIMESTAMP
);

create index file_records_path on file_records(backup_path);
