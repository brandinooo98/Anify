import "./Loading.css";

const Loading = () => {
  return (
    <div className="loader-container">
      <h1 className="loading-text">This may take a while...</h1>
      <div className="spinner"></div>
    </div>
  );
};

export default Loading;
