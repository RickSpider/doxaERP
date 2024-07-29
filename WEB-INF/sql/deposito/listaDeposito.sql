Select d.depositoid, d.nombre from depositos d
where d.empresaid = ?1
order by d.depositoid asc;