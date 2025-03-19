# Book API Documentation

## Base URL

All API endpoints now start with: `/api/books`

## Endpoints

### 1. Add a Book (add_book)

* **Method:** `POST`
* **URL:** `/api/books/add`
* **Description:** Adds a new book to the library. Only librarians can do this!
* **Request Body:** A special note (JSON) with book details:
    ```json
    {
      "username": "librarian_username",
      "title": "Book Title",
      "author": "Author Name",
      "publisher": "Publisher Name",
      "year": 2024,
      "price": "19.99",
      "genres": ["Fantasy", "Adventure"],
      "synopsis": "A short summary of the book.",
      "content": "The full book text."
    }
    ```
* **Response:**
    * **Success (201 Created):**
        ```json
        {
          "status": "success",
          "message": "Book added successfully.",
          "data": null
        }
        ```
    * **Error:** If the book is already in the library or the details are wrong, we'll send an error.

### 2. Borrow a Book (borrow_book)

* **Method:** `POST`
* **URL:** `/api/books/borrow`
* **Description:** Lets a user borrow a book.
* **Request Body:** A note (JSON) with borrowing details:
    ```json
    {
      "username": "user_username",
      "title": "Book Title",
      "days": 7
    }
    ```
* **Response:**
    * **Success (201 Created):**
        ```json
        {
          "status": "success",
          "message": "Book added successfully.",
          "data": null
        }
        ```
    * **Error:** If the book is already borrowed or the user doesn't have enough borrowing credit, we'll send an error.

### 3. Add a Review (add_review)

* **Method:** `POST`
* **URL:** `/api/books/add_review`
* **Description:** Adds a review for a book.
* **Request Body:** A note (JSON) with review details:
    ```json
    {
      "username": "user_username",
      "title": "Book Title",
      "rate": 5,
      "comment": "This book was awesome!"
    }
    ```
* **Response:**
    * **Success (201 Created):**
        ```json
        {
          "status": "success",
          "message": "Review added successfully",
          "data": null
        }
        ```
    * **Error:** If the book or user isn't found or the rating is wrong, we'll send an error.

### 4. Show Book Details (show_book_details)

* **Method:** `GET`
* **URL:** `/api/books/details?title=BookTitle`
* **Description:** Shows the details of a specific book.
* **Parameters:**
    * `title`: The book's title.
* **Response:**
    * **Success (200 OK):** A note (JSON) with book details.
    * **Error:** If the book isn't found, we'll send an error.

### 5. Show Book Content (show_book_content)

* **Method:** `GET`
* **URL:** `/api/books/content?title=BookTitle&username=user_username`
* **Description:** Shows the content of a book to a user who has borrowed it.
* **Parameters:**
    * `title`: The book's title.
    * `username`: The user's name.
* **Response:**
    * **Success (200 OK):** A note (JSON) with the book's content.
    * **Error:** If the book isn't found or the user hasn't borrowed it, we'll send an error.

### 6. Show Book Reviews (show_book_reviews)

* **Method:** `GET`
* **URL:** `/api/books/reviews?title=BookTitle&page=1&pageSize=10`
* **Description:** Shows reviews for a book, with page options.
* **Parameters:**
    * `title`: The book's title.
    * `page` (optional): The page number (default: 1).
    * `pageSize` (optional): Reviews per page (default: 10).
* **Response:**
    * **Success (200 OK):** A note (JSON) with book reviews.
    * **Error:** If the book isn't found, we'll send an error.

### 7. Search Books by Title (search_books_by_title)

* **Method:** `GET`
* **URL:** `/api/books/search_by_title?title=SearchTerm&page=1&pageSize=10`
* **Description:** Searches books by title, with page options.
* **Parameters:**
    * `title`: What you're searching for.
    * `page` (optional): The page number (default: 1).
    * `pageSize` (optional): Books per page (default: 10).
* **Response:**
    * **Success (200 OK):** A note (JSON) with a list of books.
    * **Error:** If no books are found, we'll send an error.

### 8. Search Books by Author (search_books_by_author)

* **Method:** `GET`
* **URL:** `/api/books/search_by_author?name=AuthorName&page=1&pageSize=10`
* **Description:** Searches books by author, with page options.
* **Parameters:**
    * `name`: The author's name.
    * `page` (optional): The page number (default: 1).
    * `pageSize` (optional): Books per page (default: 10).
* **Response:**
    * **Success (200 OK):** A note (JSON) with a list of books.
    * **Error:** If no books are found, we'll send an error.

### 9. Search Books by Genre (search_books_by_genre)

* **Method:** `GET`
* **URL:** `/api/books/search_by_genre?genre=GenreName&page=1&pageSize=10`
* **Description:** Searches books by genre, with page options.
* **Parameters:**
    * `genre`: The book's genre.
    * `page` (optional): The page number (default: 1).
    * `pageSize` (optional): Books per page (default: 10).
* **Response:**
    * **Success (200 OK):** A note (JSON) with a list of books.
    * **Error:** If no books are found, we'll send an error.

### 10. Search Books by Year (search_books_by_year)

* **Method:** `GET`
* **URL:** `/api/books/search_by_year?from=2020&to=2023&page=1&pageSize=10`
* **Description:** Searches books by year, with page options.
* **Parameters:**
    * `from`: The start year.
    * `to`: The end year.
    * `page` (optional): The page number (default: 1).
    * `pageSize` (optional): Books per page (default: 10).
* **Response:**
    * **Success (200 OK):** A note (JSON) with a list of books.
    * **Error:** If no books are found, we'll send an error.

### 11. Advanced Book Search (search_books)

* **Method:** `GET`
* **URL:** `/api/books/searchBooks?title=Title&author=Author&genre=Genre&fromYear=2020&toYear=2023&sortBy=averageRating&sortOrder=desc&page=1&pageSize=10`
* **Description:** Advanced search for books with multiple filters and sorting options with pagination.
* **Parameters:** (All are optional)
    * `title`: The book title.
    * `author`: The author's name.
    * `genre`: The genre name.
    * `fromYear`: The start year.
    * `toYear`: The end year.
    * `sortBy`: How to sort the results (like `averageRating` or `reviewCount`).
    * `sortOrder`: In what order to sort (like `asc` for ascending or `desc` for descending).
    * `page`: The page number.
    * `pageSize`: How many books per page.
* **Response:**
    * **Success (200 OK):** A note (JSON) with a list of books that match your search.
    * **Error:** If there's something wrong with your search, we'll send an error.

# Cart API Documentation

## Base URL

All API endpoints now start with: `/api/carts`

## Endpoints

### 1. Add to Cart (add_cart)

* **Method:** `POST`
* **URL:** `/api/carts/add`
* **Description:** Puts a book in your shopping cart.
* **Request Body:** A special note (JSON) with your username and the book's title:
    ```json
    {
      "username": "your_username",
      "title": "Book Title"
    }
    ```
* **Response:**
    * **Success (200 OK):**
        ```json
        {
          "status": "success",
          "message": "Book added to cart successfully.",
          "data": null
        }
        ```
    * **Error:** If the book or you aren't found, we'll send an error.

### 2. Remove from Cart (remove_cart)

* **Method:** `DELETE`
* **URL:** `/api/carts/remove?username=your_username&title=BookTitle`
* **Description:** Takes a book out of your shopping cart.
* **Parameters:**
    * `username`: your username.
    * `title`: The book's title.
* **Response:**
    * **Success (200 OK):**
        ```json
        {
          "status": "success",
          "message": "Book removed from cart successfully.",
          "data": null
        }
        ```
    * **Error:** If the book or you aren't found, we'll send an error.

### 3. Purchase Cart (purchase_cart)

* **Method:** `POST`
* **URL:** `/api/carts/purchase`
* **Description:** Buys all the books in your cart.
* **Request Body:** A special note (JSON) with your username:
    ```json
    {
      "username": "your_username"
    }
    ```
* **Response:**
    * **Success (200 OK):**
        ```json
        {
          "status": "success",
          "message": "Purchase completed successfully.",
          "data": "Purchase details here"
        }
        ```
    * **Error:** If your cart is empty or you don't have enough game coins, we'll send an error.

### 4. Show Cart (show_cart)

* **Method:** `GET`
* **URL:** `/api/carts/details?username=your_username`
* **Description:** Shows you what's in your shopping cart.
* **Parameters:**
    * `username`: your username.
* **Response:**
    * **Success (200 OK):**
        ```json
        {
          "status": "success",
          "message": "Cart details retrieved successfully",
          "data": "List of books in your cart"
        }
        ```
    * **Error:** If you aren't found, we'll send an error.