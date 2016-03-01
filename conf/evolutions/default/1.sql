# --- !Ups
CREATE EXTENSION "uuid-ossp";
create table "IMAGES" ("UUID" UUID NOT NULL PRIMARY KEY DEFAULT uuid_generate_v1mc(),  "IMAGE" BYTEA NOT NULL, "TIMESTAMP" TIMESTAMP NOT NULL);

# --- !Downs

drop table "IMAGES";
DROP EXTENSION "uuid-ossp";