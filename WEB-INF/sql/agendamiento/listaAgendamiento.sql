select a.agendamientoid, a.titulo, a.inicio, a.fin 
from agendamientos a
where a.empresaid = ?1
order by agendamientoid asc;