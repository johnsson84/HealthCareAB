import styled from "styled-components";
import { useState } from "react";
import axios from "axios";
import { useAuth } from "../../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import "./Signup.css";

const Title = styled.h2`
  font-size: 22px;
`;

function Signup() {
  const { setAuthState } = useAuth();
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
    mail: "",
    firstName: "",
    lastName: "",
  });
  const [error, setError] = useState("");

  const handleInputChange = (e) => {
    setCredentials((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleNavigateToLogin = () => {
    navigate("/home");
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        `${import.meta.env.VITE_API_URL}/auth/register`,
        credentials,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      const { username, roles } = response.data;

      setAuthState({
        isAuthenticated: true,
        user: username,
        roles: roles,
        loading: false,
      });

      // redirect based on role
      if (roles.includes("ADMIN")) {
        navigate("/admin/dashboard", { replace: true });
      } else {
        navigate("/user/dashboard", { replace: true });
      }
    } catch (error) {
      console.log(error);
      console.log(error.response);

      setError("Fill in inputs correctly.");
    }
  };

  return (
    <div className="loginContainer">
      <Title>Register</Title>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <form className="formWrapper" onSubmit={handleSignup}>
        <p>* = Mandatory</p>
        <label>* Username: </label>
        <input
          className="styledInput"
          name="username"
          type="text"
          value={credentials.username}
          onChange={handleInputChange}
          required
        />
        <label>* Password: </label>
        <input
          className="styledInput"
          name="password"
          type="password"
          value={credentials.password}
          onChange={handleInputChange}
          required
        />
        <label>* Mail:</label>
        <input
          className="styledInput"
          name="mail"
          type="mail"
          value={credentials.mail}
          onChange={handleInputChange}
          required
        />
        <label>* First Name:</label>
        <input
          className="styledInput"
          name="firstName"
          type="text"
          value={credentials.firstName}
          onChange={handleInputChange}
          required
        />
        <label>* Last Name:</label>
        <input
          className="styledInput"
          name="lastName"
          type="text"
          value={credentials.lastName}
          onChange={handleInputChange}
          required
        />
        <button className="loginButton" type="submit">
          Sign Up
        </button>
      </form>
      <div className="haveAccountContainer">
        <button className="haveAccountButton" onClick={handleNavigateToLogin}>
          Already have an account?
        </button>
      </div>
    </div>
  );
}

export default Signup;
