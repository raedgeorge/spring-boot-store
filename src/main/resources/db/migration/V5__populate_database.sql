INSERT INTO categories (name)
VALUES ('Dairy'),
       ('Bakery'),
       ('Beverages'),
       ('Produce'),
       ('Snacks');

INSERT INTO products (name, price, description, category_id)
VALUES ('Whole Milk', 3.49, '1 gallon of whole cow milk, rich in calcium and vitamin D.', 1),
       ('Cheddar Cheese Block', 4.99, 'Aged cheddar cheese block, great for slicing or melting.', 1),
       ('Sourdough Bread', 2.99, 'Freshly baked sourdough loaf with a crispy crust.', 2),
       ('Chocolate Croissant', 1.99, 'Flaky croissant filled with rich dark chocolate.', 2),
       ('Orange Juice', 3.79, '100% pure squeezed orange juice, no added sugar.', 3),
       ('Green Tea (12 Pack)', 6.49, 'Bottled unsweetened green tea, 16 oz bottles.', 3),
       ('Bananas (1 lb)', 0.69, 'Fresh ripe bananas sold per pound.', 4),
       ('Romaine Lettuce', 1.29, 'Crisp romaine hearts, perfect for salads.', 4),
       ('Salted Potato Chips', 2.49, 'Classic salted potato chips, 8 oz bag.', 5),
       ('Granola Bars (6 Pack)', 3.99, 'Oats and honey granola bars, great for on-the-go.', 5);
