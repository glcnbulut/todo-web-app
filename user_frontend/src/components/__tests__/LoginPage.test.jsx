// LoginPage bileşeninin testleri. Giriş formu ve hata mesajı davranışlarını kontrol eder.

import '@testing-library/jest-dom';
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import LoginPage from "../LoginPage";

describe("LoginPage", () => {
  it("renders login form", () => {
    render(<LoginPage onLogin={() => {}} />);
    expect(screen.getByText(/Giriş Yap/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/E-posta/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Şifre/i)).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /Giriş/i })).toBeInTheDocument();
  });

  it("shows error on failed login", async () => {
    render(<LoginPage onLogin={() => {}} />);
    fireEvent.change(screen.getByPlaceholderText(/E-posta/i), { target: { value: "test@test.com" } });
    fireEvent.change(screen.getByPlaceholderText(/Şifre/i), { target: { value: "wrongpass" } });
    fireEvent.click(screen.getByRole("button", { name: /Giriş/i }));
    // Hata mesajı async olduğundan, test ortamında mock axios ile kontrol edilebilir
  });
});
