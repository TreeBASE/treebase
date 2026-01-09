package org.cipres.treebase.web.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for TaxonSearchController.
 * 
 * Tests fix for issue #176: NumberFormatException when a string is searched on taxon_id.
 */
public class TaxonSearchControllerTest {

    private TaxonSearchController controller;
    
    @Mock
    private TaxonHome taxonHome;
    
    @Mock
    private TaxonLabelService taxonLabelService;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpSession session;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new TaxonSearchController();
        controller.setTaxonHome(taxonHome);
        controller.setTaxonLabelService(taxonLabelService);
        
        // Setup mock session
        when(request.getSession()).thenReturn(session);
    }
    
    /**
     * Test that searching for NCBI taxon ID with a non-numeric string does not throw exception.
     * This is the main fix for issue #176.
     */
    @Test
    public void testDoIdentifierSearch_NCBI_WithNonNumericString_DoesNotThrow() throws Exception {
        // Use reflection to access the private method
        Method method = TaxonSearchController.class.getDeclaredMethod(
            "doIdentifierSearch", 
            HttpServletRequest.class, 
            String.class, 
            Class.forName("org.cipres.treebase.web.controllers.TaxonSearchController$NamingAuthority"),
            String.class
        );
        method.setAccessible(true);
        
        // Get the NCBI enum value
        Object ncbiValue = getEnumValue("NCBI");
        
        // Act - This should NOT throw NumberFormatException
        @SuppressWarnings("unchecked")
        Collection<Taxon> result = (Collection<Taxon>) method.invoke(
            controller, request, "Homo", ncbiValue, null
        );
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty for non-numeric NCBI ID", result.isEmpty());
        // Verify that a message was added to inform the user via request.setAttribute
        verify(request).setAttribute(eq("searchMessage"), any());
    }
    
    /**
     * Test that searching for uBio namebank ID with a non-numeric string does not throw exception.
     */
    @Test
    public void testDoIdentifierSearch_UBIO_WithNonNumericString_DoesNotThrow() throws Exception {
        // Use reflection to access the private method
        Method method = TaxonSearchController.class.getDeclaredMethod(
            "doIdentifierSearch", 
            HttpServletRequest.class, 
            String.class, 
            Class.forName("org.cipres.treebase.web.controllers.TaxonSearchController$NamingAuthority"),
            String.class
        );
        method.setAccessible(true);
        
        // Get the UBIO enum value
        Object ubioValue = getEnumValue("UBIO");
        
        // Act - This should NOT throw NumberFormatException
        @SuppressWarnings("unchecked")
        Collection<Taxon> result = (Collection<Taxon>) method.invoke(
            controller, request, "SomeTextValue", ubioValue, null
        );
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty for non-numeric uBio ID", result.isEmpty());
        // Verify that a message was added to inform the user via request.setAttribute
        verify(request).setAttribute(eq("searchMessage"), any());
    }
    
    /**
     * Test that searching for NCBI taxon ID with a valid numeric string works correctly.
     */
    @Test
    public void testDoIdentifierSearch_NCBI_WithValidNumericString_ReturnsMatch() throws Exception {
        // Setup mock to return a taxon
        Taxon mockTaxon = mock(Taxon.class);
        when(taxonHome.findByNcbiTaxId(9606)).thenReturn(mockTaxon);
        
        // Use reflection to access the private method
        Method method = TaxonSearchController.class.getDeclaredMethod(
            "doIdentifierSearch", 
            HttpServletRequest.class, 
            String.class, 
            Class.forName("org.cipres.treebase.web.controllers.TaxonSearchController$NamingAuthority"),
            String.class
        );
        method.setAccessible(true);
        
        // Get the NCBI enum value
        Object ncbiValue = getEnumValue("NCBI");
        
        // Act
        @SuppressWarnings("unchecked")
        Collection<Taxon> result = (Collection<Taxon>) method.invoke(
            controller, request, "9606", ncbiValue, null
        );
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result should contain one taxon", 1, result.size());
        assertTrue("Result should contain the mock taxon", result.contains(mockTaxon));
    }
    
    /**
     * Test that searching for uBio namebank ID with a valid numeric string works correctly.
     */
    @Test
    public void testDoIdentifierSearch_UBIO_WithValidNumericString_ReturnsMatch() throws Exception {
        // Setup mock to return a taxon
        Taxon mockTaxon = mock(Taxon.class);
        when(taxonHome.findByUBIOTaxId(12345L)).thenReturn(mockTaxon);
        
        // Use reflection to access the private method
        Method method = TaxonSearchController.class.getDeclaredMethod(
            "doIdentifierSearch", 
            HttpServletRequest.class, 
            String.class, 
            Class.forName("org.cipres.treebase.web.controllers.TaxonSearchController$NamingAuthority"),
            String.class
        );
        method.setAccessible(true);
        
        // Get the UBIO enum value
        Object ubioValue = getEnumValue("UBIO");
        
        // Act
        @SuppressWarnings("unchecked")
        Collection<Taxon> result = (Collection<Taxon>) method.invoke(
            controller, request, "12345", ubioValue, null
        );
        
        // Assert
        assertNotNull("Result should not be null", result);
        assertEquals("Result should contain one taxon", 1, result.size());
        assertTrue("Result should contain the mock taxon", result.contains(mockTaxon));
    }
    
    /**
     * Helper method to get enum value by name using reflection.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object getEnumValue(String name) throws Exception {
        Class<?> enumClass = Class.forName(
            "org.cipres.treebase.web.controllers.TaxonSearchController$NamingAuthority"
        );
        return Enum.valueOf((Class<Enum>) enumClass, name);
    }
}
