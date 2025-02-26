# Maxi-Pet  

Maxi-Pet is a web application that replicates the functionality of an online pet store, allowing customers to browse products, add them to their shopping carts, place orders, and leave reviews. Administrators can manage products, users, orders, and promotions through a secure dashboard.  

---

## Features  

### For Customers:  
- 🛒 **Browse Products**: Explore and search for products by category.  
- ➕ **Add to Cart**: Add products to the shopping cart and adjust quantities.  
- 📦 **Place Orders**: Complete purchases and receive email confirmations.  
- ⭐ **Leave Reviews**: Write reviews for purchased products.  

### For Administrators:  
- 👥 **Manage Users**: Create, update, and delete user accounts.  
- 🛍️ **Manage Products and Orders**: CRUD operations for products and orders.  
- 🎁 **Promotions**: Create and manage product promotions.  
- 📊 **Reports**: Generate monthly sales reports in CSV format.  
- ✉️ **Invoices**: Send invoices via email.  

---

## Architecture  
Maxi-Pet is built using the **Model-View-Controller (MVC)** architecture, ensuring a clean separation between business logic, data handling, and user interface components. The application is divided into:  
- **Frontend**: Developed with Thymeleaf for dynamic content rendering.  
- **Backend**: Built with Java and Spring Boot, handling application logic, data processing, and communication with the database.  
- **Database**: Uses PostgreSQL for efficient management of product, user, order, and review data.  
- **Messaging System**: RabbitMQ is used for asynchronous communication, particularly for order confirmation emails and inventory updates.  

---

## Technologies Used  
- **Java** and **Spring Boot** for backend logic.  
- **Thymeleaf** for frontend templating.  
- **PostgreSQL** for relational database management.  
- **RabbitMQ** for messaging and asynchronous processing.  
- **Docker** for containerization and deployment.  

---

## Installation  
1. **Clone the repository:**  
   ```bash
   git clone [repository URL]
   cd maxi-pet
2. **Build the project using Maven:**
    ```bash
    mvn clean install
3. **Start the application using Docker:**
    ```bash
    docker-compose up
4. **Access the application at:**
    ```bash
    http://localhost:8080
## Usage  
- Customers can explore products without an account but need to register or log in to place orders and write reviews.  
- Administrators have access to a dashboard for managing users, products, orders, and promotions.  

---

## Future Enhancements  
- 🎯 **Personalized Offers**: Customized promotions and loyalty programs.  
- 💳 **Payment Integration**: Support for various payment and delivery methods.  
- 📢 **Marketing Integration**: Integration with third-party marketing platforms.  