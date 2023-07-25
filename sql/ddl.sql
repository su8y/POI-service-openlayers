create table MEMBER(
 id VARCHAR(50) primary key,
 password VARCHAR(50) not null,
 email VARCHAR(50) unique,
 role BOOLEAN default false
);
CREATE TABLE poi (
	id serial4 NOT NULL,
	"name" varchar(50) NOT NULL,
	tel_no varchar(15) NULL,
	category_code int4 NULL,
	coordinates public.geometry NULL,
	description varchar(500) NULL,
	member_id varchar(50) NULL,
	lon float4 NULL,
	lat float4 NULL,
	CONSTRAINT poi_pkey PRIMARY KEY (id),
	CONSTRAINT fk_category_id FOREIGN KEY (category_code) REFERENCES public.category(category_code),
	CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES public."member"(id) ON DELETE CASCADE ON UPDATE CASCADE
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

