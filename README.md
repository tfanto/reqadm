"# devmgr" 

select * from reqadm.oper where topicname='customer' and dltusr notnull;
select * from reqadm.oper where  topicname='customer' and dltusr isnull;
