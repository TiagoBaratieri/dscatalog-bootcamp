import { AxiosRequestConfig } from 'axios';
import Pagination from 'components/Pagination';
import ProductCard from 'components/ProductCard/indexs';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Product } from 'type/product';
import { SpringPage } from 'type/vendor/spring';
import { requestBackend } from 'util/request';
import CardLoader from './CardLoader';
import './styles.scss';

const Catalog = () => {
  const [page, setPage] = useState<SpringPage<Product>>();
  const [isloading, setIsloading] = useState(false);

  useEffect(() => {
    const params: AxiosRequestConfig = {
      method: 'GET',
      url: "/products",
      params: {
        page: 0,
        size: 12,
      },
    };

    setIsloading(true);
    requestBackend(params)
      .then((response) => {
        setPage(response.data);
      })
      .finally(() => {
        setIsloading(false);
      });
  }, []);

  return (
    <div className="container my-4 catolog-container">
      <div className="row catalog-title-container">
        <h1>Catálogo de produtos</h1>
      </div>
      <div className="row">
        {isloading ? <CardLoader / > : (
          page?.content.map((product) => {
          return (
            <div className="col-sm-6 col-lg-4 col-xl-3" key={product.id}>
              <Link to="/products/1">
                <ProductCard product={product} />
              </Link>
            </div>
          );
        }))}
        <div className="row">
          <Pagination />
        </div>
      </div>
    </div>
  );
};

export default Catalog;
