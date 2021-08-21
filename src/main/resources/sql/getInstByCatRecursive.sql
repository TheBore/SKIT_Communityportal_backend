create or replace function categories_recursive_function(bigint)
    returns TABLE(id bigint)
    language sql
as
$$
with recursive ancestry as (
    select ic.id
    from institution_category ic
    where ic.id = $1

    union all

    select icr.id
    from institution_category icr
    inner join ancestry a on a.id = icr.parent_category_id
)
select *
from ancestry
$$;