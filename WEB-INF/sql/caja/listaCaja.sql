Select c.cajaid, s.nombre, c.usuarioCajaid, c.fechaapertura, c.usuariocierreid, c.fechacierre
from cajas c
join sucursales s on s.sucursalid = c.sucursalid
where c.empresaid = ?1
order by cajaid desc;