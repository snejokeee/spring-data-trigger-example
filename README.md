## How can I implement the trigger functions in regular java?

This is an `SQL` code for trigger function:

```postgresql
CREATE OR REPLACE FUNCTION order_details_change_trigger_func()
 RETURNS trigger AS
$$
BEGIN
    IF (TG_OP != 'DELETE') THEN
 update orders set total_price = 
 (select sum(p.price * o.quantity)
 from products p, order_details o
 where p.articul = o.articul
 and o.order_id = new.order_id
 )
 where id = new.order_id;
 return new;
    ELSE
 update orders set total_price = 
 COALESCE((select sum(p.price * o.quantity)
 from products p, order_details o
 where p.articul = o.articul
 and o.order_id = old.order_id
 ),0)
 where id = old.order_id;
 return old;
    END IF;
END;
$$
    LANGUAGE 'plpgsql';
CREATE TRIGGER order_details_change_trigger
 AFTER INSERT OR DELETE OR UPDATE OF articul, quantity
 ON order_details
 FOR EACH ROW
 EXECUTE PROCEDURE order_details_change_trigger_func();
```