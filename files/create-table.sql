drop table if exists ping cascade;
CREATE TABLE ping (id SERIAL PRIMARY KEY, node TEXT, timestamp TEXT) ;
select bdr.alter_sequence_set_kind('ping_id_seq', 'galloc');
