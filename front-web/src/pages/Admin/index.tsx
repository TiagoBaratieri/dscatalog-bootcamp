import Navbar from './Navbar';
import './styles.scss';
const Admin = () => {
  return (
    <div className="navbar-container">
      <Navbar />
      <div className="admin-content">
        <h1>Contéudo</h1>
      </div>
    </div>
  );
};

export default Admin;
