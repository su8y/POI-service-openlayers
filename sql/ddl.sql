create table MEMBER(
 id VARCHAR(50) primary key,
 password VARCHAR(50) not null,
 email VARCHAR(50) unique,
 role BOOLEAN default false
);
create table POI(
	id SERIAL primary key,
	name VARCHAR(50) not null,
	tel_no VARCHAR(15),
	category_id INTEGER, -- to be : must make category_id constraint
	coordinates geometry not null, 
	description VARCHAR(500),
	member_id VARCHAR(50) not null,
	CONSTRAINT fk_member_id foreign key(member_id) references MEMBER(id)
	on delete cascade 
	on UPDATE cascade
);
create table route(
	id SERIAL primary key,
	member_id VARCHAR(50) not null,
	start_position geometry not null,
	end_position geometry not null,
	description VARCHAR(200),
	-- to be : make table information 
	constraint fk_member_id foreign key(member_id) references MEMBER(id)
	on delete cascade
	on update cascade
);
create table waypoint(
	id SERIAL primary key,
	route_id INTEGER not null,
	point geometry not null,
	constraint fk_route_id foreign key(route_id) references route(id)
	on delete cascade 
	on update cascade 
);
create table large_category(
	id SERIAL primary key,
	name VARCHAR(20)
);
create table middle_category(
	id SERIAL primary key,
	large_category_id INTEGER ,
	name VARCHAR(20),
	constraint fk_large_category_id foreign key(large_category_id)
	references large_category(id)
);
create table small_category(
	id SERIAL primary key,
	middle_category_id INTEGER ,
	name VARCHAR(20),
	constraint fk_middle_category_id foreign key(middle_category_id)
	references middle_category(id)
);
create table sub_category(
	id SERIAL primary key,
	small_category_id INTEGER ,
	name VARCHAR(20),
	constraint fk_small_category_id foreign key(small_category_id)
	references small_category(id)
);
create table category_integration(
	id SERIAL primary key,
	name varchar(20),
	sub_category_id INTEGER,
	constraint fk_sub_category foreign key(sub_category_id)
	references sub_category(id)
);
alter table POI add constraint fk_category_id foreign key(category_id)
references category_integration(id);

