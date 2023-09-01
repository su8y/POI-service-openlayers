create table MEMBER(
                       id VARCHAR primary key,
                       password VARCHAR not null,
                       email VARCHAR unique,
                       role VARCHAR default false
);
CREATE TABLE poi (
                     id serial4 NOT NULL,
                     "name" varchar NOT NULL,
                     tel_no varchar NULL,
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
                      member_id VARCHAR not null,
                      title VARCHAR not null,
                      start_position geometry not null,
                      end_position geometry not null,
                      description VARCHAR,
                      waypoints geometry,
                      json text,
                      CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES public."member"(id)
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

create table upload_image(
                             store_filename varchar,
                             original_filename varchar not null,
                             ext varchar(10) not null,
                             create_at timestamp,
                             target_id integer not null,
                             type varchar(5) not null,
                             constraint fk_upload_image_id primary key(store_file_name)
)
