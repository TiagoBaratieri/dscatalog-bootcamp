import axios from 'axios';
import Pagination from 'components/Pagination';
import ProductCard from 'components/ProductCard/indexs';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Product } from 'type/product';
import { AxiosParams } from 'type/vendor/axios';
import { SpringPage } from 'type/vendor/spring';
import { BASE_URL } from 'util/request';
import './styles.scss';

const Catalog = () => {
  const [page, setPage] = useState<SpringPage<Product>>();

  useEffect(() => {
    const params: AxiosParams = {
      method: 'GET',
      url: `${BASE_URL}/products`,
      params: {
        page: 0,
        size: 12,
      },
    };

    axios(params).then((response) => {
      setPage(response.data);
    });
  }, []);

  return (
    <div className="container my-4 catolog-container">
      <div className="row catalog-title-container">
        <h1>Catálogo de produtos</h1>
      </div>
      <div className="row">
        {page?.content.map((product) => {
          return(
          <div className="col-sm-6 col-lg-4 col-xl-3" key={product.id}>
            <Link to="/products/1">
              <ProductCard product={product} />
            </Link>
          </div>
          );
        })}
        <div className="row">
          <Pagination />
        </div>
      </div>
    </div>
  );
};

export default Catalog;
