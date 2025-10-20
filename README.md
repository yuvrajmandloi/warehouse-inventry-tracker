I defined a Product class to hold product information such as ID, name, quantity, and reorder level. Next, I defined a Warehouse class to handle all products in a HashMap. The warehouse supports adding products, receiving shipments, and filling orders.

For automatic handling of low-stock alerts, I applied the Observer design pattern. I established an AlertService interface and a RestockAlertService class, which gets notified whenever the quantity of a product drops below its threshold.

Exception handling was properly implemented for illegal product IDs as well as insufficient stock conditions.
The design encapsulates, modularizes, and updates in real-time, while also bypassing databases by keeping all data in memory.

It gives a pure, event-driven solution for warehouse inventory management that is easily extensible for file persistence or multiple warehouses.
