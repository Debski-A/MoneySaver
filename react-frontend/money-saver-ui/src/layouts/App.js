import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Navigation from './Navigation';
import Page from './Page';

function App() {
  return (
    <Router>
      <div className="app">
        <header>
          <h1>Some Header</h1>
        </header>
        <main>
          <aside>
            <Navigation />
          </aside>
          <section>
            <Page />
          </section>
        </main>
        <footer>
          <h1>Some footer</h1>
        </footer>
      </div>
    </Router>
  );
}

export default App;
