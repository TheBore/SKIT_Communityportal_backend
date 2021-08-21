create or replace function institutions_recursive_function(bigint)
    returns TABLE(id bigint)
    language sql
as
$$
with recursive ancestry as (
    select i.id
    from institution i
    where i.id = $1

    union all

    select ins.id
    from institution ins
             inner join ancestry a on a.id = ins.parent_institution_id
)
select *
from ancestry
         $$;