Select c.comprobanteid, c.timbrado, s.establecimiento, c.puntoexpedicion, c.emision, c.vencimiento, c.inicio, c.fin, c.activo 
from comprobantes c
join sucursales s on s.sucursalid = c.sucursalid
where c.empresaid = ?1
order by c.comprobanteid desc;