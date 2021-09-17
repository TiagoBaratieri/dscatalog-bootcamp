import { AxiosRequestConfig } from 'axios';
import ProductCrudCard from 'components/ProductCrudCard/indexs';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Product } from 'type/product';
import { SpringPage } from 'type/vendor/spring';
import { requestBackend } from 'util/request';
import './styles.scss';

const List = () => {
  const [page, setPage] = useState<SpringPage<Product>>();

    useEffect(() => {
      getProducts();
    }, []);

    const getProducts = () => {
      const config: AxiosRequestConfig = {
        method: 'GET',
        url: '/products',
        params: {
          page: 0,
          size: 50,
        },
      };

    requestBackend(config).then((response) => {
      setPage(response.data);
    });
  };

  return (
    <div className="product-crud-container">
      <div className="product-crud-bar-container">
        <Link to="/admin/products/created">
          <button className="btn btn-primary text-white btn-crud-add">
            ADICIONAR
          </button>
        </Link>

        <div className="base-card product-filter-container">Search Bar</div>
      </div>
      <div className="row">
        {page?.content.map((product) => (
          <div key={product.id} className="col-sm-6 col-md-12">
           <ProductCrudCard product={product} onDelete={getProducts} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default List;
