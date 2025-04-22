import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import HomePage from './pages/HomePage';
import SearchResult from './pages/SearchResult';
import BookPage from './pages/BookPage';
import UserPage from './pages/UserPage';
import BuyCartPage from './pages/BuyCartPage';
import ErrorPage from './pages/ErrorPage';
import Error404Page from './pages/Error404Page';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/signin" element={<SignInPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/results" element={<SearchResult />} />
        <Route path="/book" element={<BookPage />} />
        <Route path="/user" element={<UserPage />} />
        <Route path="/cart" element={<BuyCartPage />} />
        <Route path="/error" element={<ErrorPage />} />
        <Route path="/404" element={<Error404Page />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;