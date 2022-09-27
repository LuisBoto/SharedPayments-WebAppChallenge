create table user (
        id bigint not null,
        debt decimal(19,2), 
        name varchar(255),
        primary key (id));
	
create table payment (
        id bigint not null, 
        description varchar(255), 
        payment_date bigint, 
        price decimal(19,2),
        payer_id bigint not null,
        FOREIGN KEY (payer_id) REFERENCES user(id),
        PRIMARY KEY (id));
        
 insert into user (id, name, debt) values (1, "Pepe", 10.00);
	