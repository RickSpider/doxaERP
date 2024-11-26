Select p.productoid, m.nombre ,p.nombre , tum.tipo, p.precioventa, tiva.tipo
from productos p
join tipos tiva on tiva.tipoid = p.ivatipoid
join tipos tum on tum.tipoid = p.unidadmedidatipoid
join marcas m on m.marcaid = p.marcaid
where p.empresaid = ?1
and p.productotipoid = ?2
order by productoid asc;