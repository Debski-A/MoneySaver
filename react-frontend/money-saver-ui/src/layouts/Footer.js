import React from "react";

const footerStyle = {
  backgroundColor: "#f8f9fa",
  fontSize: "16px",
  color: "grey",
  textAlign: "center",
  padding: "10px",
  position: "fixed",
  left: "0",
  bottom: "0",
  height: "5vh",
  width: "100%"
};

const phantomStyle = {
  display: "block",
  padding: "10px",
  height: "5vh",
  width: "100%"
};

const Footer = ({ children }) => {
  return (
    <div>
      <div style={phantomStyle} />
      <div style={footerStyle}>{children}</div>
    </div>
  );
}

export default Footer