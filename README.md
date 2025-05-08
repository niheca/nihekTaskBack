
Para la paginanción
-------
En el response : 

count -> todos los productos que hay

pages -> count-offset / limit

tasks[]

---------------------------------

limit -> cuantos quiero por pagina (si tengo 30 prod y pongo un limite de >30 solo hay una pagina),
es decir que si quiero 5 tareas por pagina pongo un limit de 5 

offset ->  cuantos se quiere saltar, si tengo un limit de 5 y estoy en la pagina 3 el offset sería 
2 * 5 = 10 , es decir se salta los 10 primeros elementos que corresponden con la primera y segund pagina 
