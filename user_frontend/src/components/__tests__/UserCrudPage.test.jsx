import '@testing-library/jest-dom';
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import UserCrudPage from "../UserCrudPage";

describe("UserCrudPage", () => {
  const dummyToken = "testtoken";
  it("renders user list and form", () => {
    render(<UserCrudPage token={dummyToken} onLogout={() => {}} />);
    expect(screen.getByText(/Kullanıcılar/i)).toBeInTheDocument();
    expect(screen.getByText(/Kullanıcı Ekle/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Çıkış Yap/i })).toBeInTheDocument();
  });

  it("renders add user form fields", () => {
  render(<UserCrudPage token={dummyToken} onLogout={() => {}} />);
  // Ad ve Soyad inputları için getAllByPlaceholderText kullanıyoruz
  expect(screen.getAllByPlaceholderText(/Ad/i).length).toBeGreaterThan(0);
  expect(screen.getByPlaceholderText(/Soyad/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/E-posta/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/Şifre/i)).toBeInTheDocument();
  expect(screen.getByRole("button", { name: /Ekle/i })).toBeInTheDocument();
  });
});
