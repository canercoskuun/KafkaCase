--TEST-1 — COMPLETED & ORDER_IN_TIME = TRUE (lead_time < eta)
INSERT INTO package (
    id, arrival_for_delivery_at, arrival_for_pickup_at, cancel_reason, cancelled,
    cancelled_at, collected, collected_at, completed_at, created_at,
    customer_id, delivery_date, eta, in_delivery_at, last_updated_at,
    order_id, origin_address_id, picked_up_at, reassigned, status,
    store_id, type, user_id, waiting_for_assignment_at
)
VALUES (
           1001,
           '2021-11-13 11:05:58.045640',
           '2021-11-13 10:48:37.032078',
           NULL,
           FALSE,
           NULL,
           TRUE,
           '2021-11-13 10:47:52.828692',
           '2021-11-13 11:40:15.314340',
           '2021-11-13 10:47:52.675248',
           20002011575015,
           '2021-11-13',
           277, -- ETA
           '2021-11-13 11:05:56.861614',
           '2021-11-13 11:40:15.314340',
           123972783,
           999000020443388,
           '2021-11-13 10:49:50.278087',
           NULL,
           'COMPLETED',
           20000000004103,
           'REGULAR',
           50002010395213,
           '2021-11-13 10:47:52.675248'
       );
--TEST-2 — COMPLETED but ORDER_IN_TIME = FALSE (lead_time > eta)
INSERT INTO package (
    id, created_at, completed_at, eta, last_updated_at, status, cancelled
)
VALUES (
           1002,
           '2021-11-13 10:00:00',
           '2021-11-13 11:00:00',
           30,                     -- ETA = 30 dk, lead_time = 60 dk → FALSE
           '2021-11-13 11:00:00',
           'COMPLETED',
           FALSE
       );

INSERT INTO package (
    id, created_at, last_updated_at, cancelled, cancel_reason, cancelled_at, status
)
VALUES (
           1003,
           '2021-11-13 09:00:00',
           '2021-11-13 09:10:00',
           TRUE,
           'Customer request',
           '2021-11-13 09:05:00',
           'CANCELLED'
       );
--TEST-4 — IN_PROGRESS (completed_at = NULL → lead_time NULL, order_in_time NULL)
INSERT INTO package (
    id, created_at, last_updated_at, status, cancelled, eta,
    collected, collected_at, picked_up_at, in_delivery_at
)
VALUES (
           1004,
           '2021-11-13 10:00:00',
           '2021-11-13 10:30:00',
           'IN_DELIVERY',
           FALSE,
           60,
           TRUE,
           '2021-11-13 10:05:00',
           '2021-11-13 10:10:00',
           '2021-11-13 10:20:00'
       );
--TEST-5 — Sadece Minimum Kolonlarla INSERT (NULL toleransı testi)
INSERT INTO package (
    id, created_at, last_updated_at, status, cancelled
)
VALUES (
           1005,
           '2021-11-13 12:00:00',
           '2021-11-13 12:05:00',
           'CREATED',
           FALSE
       );
--TEST-6 — collected=false ama collected_at dolu (Tutarsız veri testi)
INSERT INTO package (
    id, created_at, last_updated_at, status, cancelled,
    collected, collected_at
)
VALUES (
           1006,
           '2021-11-13 08:00:00',
           '2021-11-13 08:20:00',
           'CREATED',
           FALSE,
           FALSE,
           '2021-11-13 08:05:00'
       );

INSERT INTO package (
    id, created_at, last_updated_at, status, cancelled, reassigned
)
VALUES (
           1007,
           '2021-11-13 07:00:00',
           '2021-11-13 07:05:00',
           'WAITING_FOR_ASSIGNMENT',
           TRUE,
           TRUE
       );

--TEST-8 — ALL NULLABLE FIELDS NULL (maksimum NULL testi)
INSERT INTO package (
    id, created_at, last_updated_at, cancelled
)
VALUES (
           1008,
           '2021-11-13 06:00:00',
           '2021-11-13 06:00:01',
           FALSE
       );

--TEST-9 — Customer_id, order_id, store_id gibi Büyük Sayı Testi
INSERT INTO package (
    id, created_at, last_updated_at, cancelled, customer_id, store_id, order_id, origin_address_id
)
VALUES (
           1009,
           '2021-11-13 11:00:00',
           '2021-11-13 11:30:00',
           FALSE,
           20002011575015,
           20000000004103,
           123972783,
           999000020443388
       );
-- TEST-10 — Tüm Alanların FULL DOLU Olduğu Büyük Örnek (End-to-End test)
INSERT INTO package (
    id, arrival_for_delivery_at, arrival_for_pickup_at, cancel_reason, cancelled,
    cancelled_at, collected, collected_at, completed_at, created_at,
    customer_id, delivery_date, eta, in_delivery_at, last_updated_at,
    order_id, origin_address_id, picked_up_at, reassigned, status,
    store_id, type, user_id, waiting_for_assignment_at
)
VALUES (
           1012,
           '2021-11-10 10:30:00.111111',
           '2021-11-10 10:05:00.222222',
           NULL,
           FALSE,
           NULL,
           TRUE,
           '2021-11-10 10:06:00.222222',
           '2021-11-10 11:00:00.333333',
           '2021-11-10 10:00:00.000000',
           20002010000001,
           '2021-11-10',
           120,
           '2021-11-10 10:20:00.444444',
           '2021-11-10 11:00:00.333333',
           123450000,
           999000020000000,
           '2021-11-10 10:10:00.555555',
           FALSE,
           'COMPLETED',
           20000000000001,
           'EXPRESS',
           50002010000001,
           '2021-11-10 10:00:00.000000'
       );