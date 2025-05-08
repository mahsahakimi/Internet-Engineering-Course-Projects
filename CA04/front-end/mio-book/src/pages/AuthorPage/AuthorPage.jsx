import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Footer from '../../components/Footer';
import Nav from '../../components/Nav';
import BookCard from '../../components/BookCard';
import AuthorInfo from '../../components/AuthorInfo';
import AuthorHeader from '../../components/AuthorHeader';
import '../../stylesheets/General.css';
import '../../stylesheets/Author.css';
import EmptySpace from '../../components/EmptySpace';

function AuthorPage() {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const authorNameFromURL = queryParams.get('name');
  const [authorDetails, setAuthorDetails] = useState(null);
  const [authorBooks, setAuthorBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAuthorData = async () => {
      setLoading(true);
      setError(null);
      setAuthorDetails(null);
      setAuthorBooks([]);

      if (!authorNameFromURL) {
        setError('Author name is missing from the URL.');
        setLoading(false);
        return;
      }

      try {
        // Fetch author details
        const detailsResponse = await fetch(
          `/api/authors/details?username=${encodeURIComponent(authorNameFromURL)}`
        );

        if (!detailsResponse.ok) {
          setError(`Failed to fetch author details: ${detailsResponse.status}`);
          return;
        }

        const detailsResponseJson = await detailsResponse.json();
        console.log('Author Details API Response:', detailsResponseJson);

        if (detailsResponseJson.status === 'success' && detailsResponseJson.data) {
          try {
            const parsedDetailsData = JSON.parse(detailsResponseJson.data);
            setAuthorDetails(parsedDetailsData);
          } catch (parseError) {
            console.error('Error parsing author details:', parseError);
            setError('Failed to parse author details.');
            return;
          }
        } else {
          setError(detailsResponseJson.message || 'Failed to retrieve author details.');
          return;
        }

        // Fetch author's books
        const booksResponse = await fetch(
          `/api/books/search_by_author?name=${encodeURIComponent(
            authorNameFromURL
          )}&page=1&pageSize=10`
        );

        if (!booksResponse.ok) {
          console.warn(`Failed to fetch author's books: ${booksResponse.status}`);
          return;
        }

        const booksResponseJson = await booksResponse.json();
        console.log('Author Books API Response:', booksResponseJson);

        if (booksResponseJson.status === 'success' && booksResponseJson.data) {
          try {
            const parsedBooksData = JSON.parse(booksResponseJson.data);
            setAuthorBooks(parsedBooksData);
          } catch (parseError) {
            console.error('Error parsing author books:', parseError);
            setError('Failed to parse author books.');
          }
        } else {
          console.warn(booksResponseJson.message || 'Failed to retrieve author\'s books.');
        }
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to load author data.');
      } finally {
        setLoading(false);
      }
    };

    fetchAuthorData();
  }, [authorNameFromURL]);

  if (loading) {
    return (
      <>
        <Nav />
        <div className="container mt-5">
          <p>Loading author details and books...</p>
        </div>
        <Footer />
      </>
    );
  }

  if (error) {
    return (
      <>
        <Nav />
        <div className="container mt-5">
          <p className="text-danger">{error}</p>
        </div>
        <Footer />
      </>
    );
  }

  if (!authorDetails) {
    return (
      <>
        <Nav />
        <div className="container mt-5">
          <p>Could not load author details.</p>
        </div>
        <Footer />
      </>
    );
  }

  return (
      <>
          <Nav />

          <AuthorHeader name={authorDetails.name} />

          <EmptySpace />
          <EmptySpace />

          <AuthorInfo author={authorDetails} bookCount={authorBooks.length} />
          <EmptySpace />

          <section className="container mb-5">
              <div className="row row-cols-1 row-cols-sm-5">
              {authorBooks.map((book) => (
                  <div className="col">
                   <BookCard book={book} />
                </div>
                ))}
              </div>
          </section>
          <Footer />
      </>
    );
}

export default AuthorPage;
