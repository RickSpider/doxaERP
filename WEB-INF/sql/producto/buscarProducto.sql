Select p.productoid as id, p.nombre as producto 
from productos p
where p.empresaid = ?1
and p.productotipoid = ?2
order by productoid asc;