create or replace function compute_bonus (
    emp_id number,
    bonus number
) return number
is
    emp_sal number;
begin
    select sal into emp_sal
    from emp
    where empno = emp_id;

    return emp_sal + bonus;
end compute_bonus;
/