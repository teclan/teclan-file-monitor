create table file_records
(
id 						INT(11) NOT NULL AUTO_INCREMENT,
path					varchar(2000),
last_modified			varchar(50),
length					varchar(50),
md5						varchar(50),
sha256					varchar(100),
description				varchar(100),
created_at        		TIMESTAMP,
updated_at        		TIMESTAMP
);

create index file_records_path on file_records(path);
