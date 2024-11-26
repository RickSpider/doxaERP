Select p.productoid as id, p.nombre as Producto, p.precioVenta as precio 
from productos p
join tipos t on t.tipoid = p.productotipoid
where p.empresaid = ?1
and t.sigla ='PRODUCTO_PRODUCTO'
order by productoid asc;