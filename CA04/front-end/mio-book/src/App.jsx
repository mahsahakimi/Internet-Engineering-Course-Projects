import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import SignInPage from './pages/SignInPage';
import SignUpPage from './pages/SignUpPage';
import HomePage from './pages/HomePage';
import SearchResult from './pages/SearchResult';
import BookPage from './pages/BookPage';
import UserPage from './pages/UserPage';
import BuyCartPage from './pages/BuyCartPage';

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
      </Routes>
    </BrowserRouter>
  );
}

export default App;