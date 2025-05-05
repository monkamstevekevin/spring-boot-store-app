-- 1) Insert categories
INSERT INTO categories (name)
VALUES ('Produce'),
       ('Dairy'),
       ('Bakery'),
       ('Meat'),
       ('Beverages'),
       ('Snacks'),
       ('Frozen'),
       ('Pantry'),
       ('Health & Beauty'),
       ('Household');

-- 2) Insert products (auto-assigned IDs, link via category_id)
INSERT INTO products (name, price, description, category_id)
VALUES ('Organic Bananas', 1.29, 'Fresh organic bananas sold per pound', 1),
       ('Whole Milk 1L', 2.49, 'Pasteurized whole milk in 1 L carton', 2),
       ('Whole Wheat Bread', 3.99, 'Baked daily whole-wheat sandwich bread', 3),
       ('Ground Beef 80/20 1lb', 5.99, 'Premium ground beef, 80% lean / 20% fat', 4),
       ('Apple Juice 100% 1L', 2.99, '100% pure apple juice, no added sugar', 5),
       ('Potato Chips 200g', 1.99, 'Salted crispy potato chips', 6),
       ('Frozen Peas 500g', 1.79, 'Flash-frozen green peas for maximum freshness', 7),
       ('Olive Oil EVOO 500ml', 9.99, 'Cold-pressed extra virgin olive oil', 8),
       ('Vitamin C Tablets', 7.49, 'Vitamin C supplement, 100 tablets per bottle', 9),
       ('Dish Soap 750ml', 3.49, 'Concentrated liquid dishwashing soap, citrus scent', 10);
