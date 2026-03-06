# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
Yes — if I were maintaining this codebase, I would refactor parts of the database access and manipulation layer. The current implementation works, but it mixes responsibilities across layers and introduces some maintainability and domain integrity risks.
```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
There are two approaches used here: API-first for Warehouse, and code-first for Product and Store.

The API-first approach has the advantage of providing a clear contract between producers and consumers. It enables automatic documentation, client SDK generation, and ensures the implementation stays aligned with the API specification. However, it can slow down development slightly because changes require updating the spec and regenerating code.

The code-first approach is faster and simpler during development since developers implement endpoints directly in code, but it can lead to weaker documentation, potential contract drift, and makes it harder for external teams to integrate.
```
----
3. Given the need to balance thorough testing with time and resource constraints, how would you prioritize and implement tests for this project? Which types of tests would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```
To balance testing quality with time and resource constraints, I would prioritize tests that provide the highest confidence with the lowest maintenance cost.

First, I would focus on unit tests for the domain logic, especially business rules like stock vs capacity, warehouse replacement rules, and archiving behavior. These tests are fast, isolated, and protect the most critical logic.

Second, I would add integration tests for the repository layer, ensuring database interactions  work correctly with the ORM and schema.

Third, I would include a small number of API or endpoint tests to verify that the REST layer correctly maps requests, responses, and validations.
```