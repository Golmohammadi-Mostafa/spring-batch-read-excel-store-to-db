http://localhost:8080/swagger-ui.html#  <br>
`you can use these operator for get data by searchFilter` :

| Basic Operator | Description         |
|----------------|---------------------|
| ==             | Equal To            |
| !=             | Not Equal To        |
| =gt=           | Greater Than        |
| =ge=           | Greater Or Equal To |
| =lt=           | Less Than           |
| =le=           | Less Or Equal To    |
| =in=           | In                  |
|=out=           | Not in              |


| Composite Operator | Description         |
|--------------------|---------------------|
| ;                  | Logical AND         |
| ,                  | Logical OR          |

`<br />
When the program is running on port 8080 you can see all Rest APIs: http://localhost:8080/swagger-ui.html#
<br />`

This is sample curl: <br>
curl --location --request GET 'http://localhost:8080/api/v1/pokemons?page=0&searchFilter=hp=ge=100;defense=le=200&size=20' \
--header 'accept: */*'

