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
CREATE TABLE public.category (
	category_code int4 NOT NULL,
	lclascd int4 NULL,
	lclasdc varchar NULL,
	mlsfccd int4 NULL,
	mlsfcdc varchar NULL,
	sclascd int4 NULL,
	sclasdc varchar NULL,
	dclascd int4 NULL,
	dclasdc varchar NULL,
	bclascd int4 NULL,
	bclasdc varchar NULL,
	rm varchar(50) NULL,
	CONSTRAINT category_pkey PRIMARY KEY (category_code)
);
alter table POI add constraint fk_category_id foreign key(category_id)
references category(category_code);

