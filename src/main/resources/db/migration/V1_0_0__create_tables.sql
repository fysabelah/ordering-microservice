create table clients (
    document varchar(255) not null,
    name varchar(255) not null,
    primary key (document)
);

create table items (
    id serial not null,
    sku varchar(255) not null,
    quantity integer not null,
    value numeric(15,4) not null,
    primary key (id)
);

create table payments (
    id serial not null,
    brand varchar(255),
    expiration_date varchar(255),
    holder varchar(255),
    installments integer,
    number varchar(255),
    security_code varchar(255),
    total numeric(15,4),
    type varchar(255) check (type in ('CREDIT','DEBIT')),
    primary key (id),
    status varchar(255) check (status in ('PENDING', 'AUTHORIZED', 'UNAUTHORIZED'))
);

create table shipments (
    id serial not null,
    freight numeric(15,4),
    tracking varchar(255),
    url_tracking varchar(255),
    number varchar(255) not null,
    street varchar(255) not null,
    block varchar(255) not null,
    cep varchar(255) not null,
    complement varchar(255),
    primary key (id)
);

create table orders (
    id serial not null,
    discounts numeric(15,4),
    motive varchar(255) check (motive in ('STOCKOUT','PAYMENT_FAILURE','REFUSED_CARRIER','CANCELED_BY_USER')),
    status varchar(255) not null check (status in ('PENDING', 'PROCESSING','WAITING_PAYMENT','PAYMENT_ACCEPT','STOCK_SEPARATION','SHIPPING_READY', 'SHIPPED', 'ON_CARRIAGE','DELIVERED','CANCELED')),
    total numeric(15,4),
    client_document varchar(255),
    payment_id integer,
    shipment_id integer,
    primary key (id),
    constraint fk_client foreign key (client_document) references clients,
    constraint fk_payment foreign key (payment_id) references payments,
    constraint fk_shipmet foreign key (shipment_id) references shipments
);

create table status_history (
    id serial not null,
    date TIMESTAMP,
    status varchar(255) not null check (status in ('PENDING', 'PROCESSING','WAITING_PAYMENT','PAYMENT_ACCEPT','STOCK_SEPARATION','SHIPPING_READY', 'SHIPPED', 'ON_CARRIAGE','DELIVERED','CANCELED')),
    primary key (id)
);

create table orders_status_history (
    order_id integer not null,
    status_history_id integer not null,
    foreign key (order_id) references orders,
    foreign key (status_history_id) references status_history,
    unique (order_id, status_history_id)
);

create table orders_items (
    order_id integer not null,
    items_id integer not null,
    foreign key (order_id) references orders,
    foreign key (items_id) references items
);