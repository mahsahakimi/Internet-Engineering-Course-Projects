import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import HomePage from './pages/HomePage';
import SearchResult from './pages/SearchResult';
import BookPage from './pages/BookPage';
import AuthorPage from './pages/AuthorPage';
import BookContentPage from './pages/BookContentPage';
import UserPage from './pages/UserPage';
import BuyCartPage from './pages/BuyCartPage';
import ErrorPage from './pages/ErrorPage';
import Error404Page from './pages/Error404Page';
import AdminPage from './pages/AdminPage';
import ErrorBoundary from './components/Error/ErrorBoundary.jsx';

function App() {
  return (
    <BrowserRouter>
        <ErrorBoundary>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/signin" element={<SignInPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/results" element={<SearchResult />} />
            <Route path="/book" element={<BookPage />} />
            <Route path="/author" element={<AuthorPage />} />
            <Route path="/book/content" element={<BookContentPage />} />
            <Route path="/user" element={<UserPage />} />
            <Route path="/cart" element={<BuyCartPage />} />
            <Route path="/admin" element={<AdminPage />} />
            <Route path="/error" element={<ErrorPage />} />
            <Route path="*" element={<Error404Page />} />
          </Routes>
      </ErrorBoundary>
    </BrowserRouter>
  );
}

export default App;