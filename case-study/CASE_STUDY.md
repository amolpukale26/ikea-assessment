# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
Cost allocation is challenging because labor, transportation, overhead, and inventory handling are shared across multiple warehouses and stores, making it hard to assign costs precisely.​
When a warehouse is replaced but reuses the same Business Unit Code, it becomes essential to maintain historical cost data for proper financial tracking and reporting.
Inconsistent or invalid location data can cause incorrect cost attribution across regions.
Reliable cost tracking requires consistent data structures, clear cost drivers (capacity, stock levels, throughput), and visibility into warehouse lifecycle events.
Questions to clarify:
What cost categories need tracking (labor, overhead, transportation)?
What level of granularity is expected—per warehouse, per store, per product?
How is location information validated and stored today?

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
Optimization focuses on improving warehouse capacity utilization, eliminating under‑ or over‑usage, and minimizing unnecessary operational costs.​[hclo365-my...epoint.com]​
Enforcing constraints such as maximum warehouses per location helps minimize infrastructure and logistics overhead.
Balancing and aligning stock levels can reduce unnecessary movement, handling, or storage expenses.
Prioritize strategies based on cost impact, operational feasibility, and ability to maintain service quality.
Questions to clarify:
Which activities drive most operational cost today?
What operational constraints (capacity, stock, location limits) affect optimization potential?
Which KPIs should be used to measure improvement effectiveness?

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Integrating the Cost Control Tool with financial systems ensures real‑time, accurate cost representation across warehouses and stores.
This integration improves financial transparency, simplifies reconciliation, and maintains correct cost history during warehouse replacements.
Consistent identifiers and stable lifecycle events (creation, replacement, archival) are essential to prevent discrepancies.
Questions to clarify:
What financial system(s) are used today and what integration methods exist (API, batch, streaming)?
Is real‑time synchronization required, or are periodic updates acceptable?
How should warehouse replacements and archiving be reflected in financial reports?

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Effective budgeting and forecasting require accurate data around capacity, stock volumes, warehouse lifecycle, and location constraints.​
Historical data plays a key role in predicting future expenses, planning capacity, and understanding expected operational changes.
The system should support modeling seasonal patterns, expected demand growth, and the cost impact of warehouse replacements.
Questions to clarify:
What time horizon should forecasts support—monthly, quarterly, yearly?
How will seasonal sales cycles or demand fluctuations be incorporated?
Are replacements or location changes factored into long‑term budget projections?

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Preserving cost history is critical because the new warehouse inherits the same Business Unit Code, and financial continuity must remain intact.​
This enables accurate comparisons between old and new warehouses, supports audits, and ensures budget accuracy.
Additional validations—such as stock matching and capacity accommodation—help prevent unexpected cost increases during transitions.
Questions to clarify:
How is historical cost tied to the new warehouse instance after replacement?
Which KPIs should be monitored to determine whether the replacement is cost‑effective?
Are transition, setup, or downtime costs included in the cost control process?

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
